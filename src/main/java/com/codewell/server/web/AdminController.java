package com.codewell.server.web;

import com.codewell.server.annotation.AdminAuthenticationNeeded;
import com.codewell.server.dto.*;
import com.codewell.server.service.*;
import com.codewell.server.util.DataValidator;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;
import java.util.List;

import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_NAME;
import static com.codewell.server.config.settings.SwaggerSettings.SWAGGER_AUTH_SCHEME;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

@Path("/v1/admin")
@SecurityScheme(name = SWAGGER_AUTH_NAME, type = HTTP, scheme = SWAGGER_AUTH_SCHEME, in = HEADER)
@Tag(name = "Admin Controller", description = "Manage admin resources")
public class AdminController
{
    private final UserService userService;
    private final UserLearningService userLearningService;
    private final SessionService sessionService;
    private final EnrollmentService enrollmentService;
    private final GradesService gradesService;

    @Inject
    public AdminController(final UserService userService,
                           final UserLearningService userLearningService,
                           final SessionService sessionService,
                           final EnrollmentService enrollmentService,
                           final GradesService gradesService)
    {
        this.userService = userService;
        this.userLearningService = userLearningService;
        this.sessionService = sessionService;
        this.enrollmentService = enrollmentService;
        this.gradesService = gradesService;
    }

    @GET
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public List<UserDto> getAllUsers(@QueryParam("sessionId") final Integer sessionId)
    {
        if (sessionId != null)
        {
            return userService.getUsersInSession(sessionId);
        }
        else
        {
            return userService.getAllUsers();
        }
    }

    @POST
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users/create")
    public UserDto createNewUser(final UserDto userDto)
    {
        Assert.notNull(userDto, "User payload must not be null");
        Assert.hasText(userDto.getUsername(), "No username provided");
        Assert.hasText(userDto.getPassword(), "No password provided");
        Assert.hasText(userDto.getEmail(), "No email provided");
        Assert.hasText(userDto.getFirstName(), "No first name provided");
        Assert.isTrue(DataValidator.isValidBoolean(userDto.getIsAdmin()), "Is admin field is not a valid boolean value");

        return userService.createUser(userDto);
    }

    @GET
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/enrollment")
    public EnrollmentDto getEnrollment(@QueryParam("userId") final String userId,
                                       @QueryParam("sessionId") final Integer sessionId)
    {
        Assert.hasText(userId, "User id must be provided");
        Assert.notNull(sessionId, "Session id must be provided");

        return enrollmentService.getEnrollmentByUserAndSession(userId, sessionId);
    }

    @POST
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/enrollment/enroll")
    public EnrollmentDto enrollStudent(@QueryParam("userId") final String userId,
                                       @QueryParam("sessionId") final Integer sessionId)
    {
        Assert.hasText(userId, "User id must be provided");
        Assert.notNull(sessionId, "Session id must be provided");

        return enrollmentService.enrollStudentToSession(userId, sessionId);
    }

    @PUT
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/enrollment/update")
    public EnrollmentDto updateEnrollment(final EnrollmentDto enrollmentDto)
    {
        Assert.notNull(enrollmentDto, "Enrollment dto cannot be null");
        Assert.notNull(enrollmentDto.getId(), "Enrollment id not provided");
        Assert.notNull(enrollmentDto.getSessionId(), "Session id not provided");
        Assert.hasText(enrollmentDto.getUserId(), "User id not provided");
        Assert.notNull(enrollmentDto.getEnrollDate(), "Enrollment date not provided");
        Assert.notNull(enrollmentDto.getCurrentChapter(), "Current chapter not provided");
        Assert.notNull(enrollmentDto.getGraduated(), "Graduation flag not provided");
        Assert.notNull(enrollmentDto.getOverallGrade(), "Overall grade not provided");

        return enrollmentService.updateEnrollmentRecord(enrollmentDto);
    }

    @GET
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/learning/{userId}")
    public UserLearningModel getUserLearningProgress(@PathParam("userId") final String userId) throws Exception
    {
        Assert.hasText(userId, "User id not provided");
        return userLearningService.getUserLearningModel(userId);
    }

    @GET
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/sessions/teaching")
    public List<SessionDto> getTeachingSessions(@Parameter(hidden = true) @HeaderParam("Source-User-Id") final String adminUserId)
    {
        Assert.hasText(adminUserId, "Admin user id not provided");
        return sessionService.getSessionsTaughtByAdmin(adminUserId);
    }

    @GET
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/grades/{userId}")
    public List<GradeDto> getGrades(@PathParam("userId") final String userId,
                                    @QueryParam("sessionId") final Integer sessionId)
    {
        Assert.hasText(userId, "User id not provided");
        Assert.notNull(sessionId, "Session id not provided");
        return gradesService.getGradesForUser(userId, sessionId);
    }

    @PUT
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/grades/update")
    public GradeDto updateGrade(@QueryParam("userId") final String userId,
                                @QueryParam("sessionId") final Integer sessionId,
                                final GradeDto gradeDto)
    {
        Assert.hasText(userId, "User id not provided");
        Assert.notNull(sessionId, "Session id not provided");
        Assert.notNull(gradeDto, "Grade dto cannot be null");
        Assert.notNull(gradeDto.getHomeworkId(), "Homework id cannot be null");
        Assert.hasText(gradeDto.getSubmitted(), "Submitted flag cannot be null");
        Assert.isTrue(DataValidator.isValidBoolean(gradeDto.getSubmitted()), "Submitted flag is not a valid boolean value");

        return gradesService.modifyGrade(userId, sessionId, gradeDto);
    }

    @PUT
    @AdminAuthenticationNeeded
    @SecurityRequirement(name = SWAGGER_AUTH_NAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/grades/update/bulk")
    public List<GradeDto> bulkUpdateGrades(@QueryParam("userId") final String userId,
                                           @QueryParam("sessionId") final Integer sessionId,
                                           final List<GradeDto> gradeDtos) throws InterruptedException
    {
        Assert.hasText(userId, "User id not provided");
        Assert.notNull(sessionId, "Session id not provided");
        Assert.notNull(gradeDtos, "Grade dto list cannot be null");
        gradeDtos.forEach(gradeDto ->
        {
            Assert.notNull(gradeDto.getHomeworkId(), "Homework id cannot be null");
            Assert.hasText(gradeDto.getSubmitted(), "Submitted flag cannot be null");
            Assert.isTrue(DataValidator.isValidBoolean(gradeDto.getSubmitted()), "Submitted flag is not a valid boolean value");
        });
        return gradesService.bulkModifyGrades(userId, sessionId, gradeDtos);
    }
}
