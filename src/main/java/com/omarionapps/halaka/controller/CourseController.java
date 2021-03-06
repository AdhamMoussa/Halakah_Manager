package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CountryService;
import com.omarionapps.halaka.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Omar on 12-May-17.
 */
@Controller
public class CourseController {
    private CourseService courseService;
    private ActivityService activityService;
    private CountryService countryService;

    @Autowired
    public CourseController(CourseService courseService, ActivityService activityService, CountryService countryService) {
        this.courseService = courseService;
        this.activityService = activityService;
        this.countryService = countryService;
    }

    /**
     * Process the get request for courses list
     *
     * @return the page of all courses list
     */
    @GetMapping("/admin/courses")
    public ModelAndView getCourses() {
        ModelAndView modelAndView = new ModelAndView("admin/course-list");
        modelAndView.addObject("courses", courseService.findAllByArchive(false));
        return modelAndView;
    }

    /**
     * Process the get course request, according to id value return the appropriate page
     * if it's null it means that the user want to create new course, otherwise it shows the page of the required course.
     *
     * @param id the request parameter that sent with the request
     * @return the page of appropriate request based on request param that sent
     */
    @GetMapping("/admin/courses/course")
    public ModelAndView processCourse(@RequestParam(value = "id", required = false) Integer id) {
        return (null == id) ? addCourse() : getCourse(id);
    }

    /**
     * Process the post request of updating existing course or creating new one
     *
     * @param course the course to be updated or created
     * @return redirect to the saved course profile page
     */
    @PostMapping("/admin/courses/course")
    public String saveCourse(@Valid Course course, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            List<FieldError> fErrors = bindingResult.getFieldErrors();
            BindingResult bResult = new BeanPropertyBindingResult(course, "course");
            for (FieldError error : fErrors) {
                String field = error.getField();
                bResult.rejectValue(field, field + " is required");
            }
            bindingResult = bResult;
            model.addAttribute("activities", activityService.findAllOrderByName());
            model.addAttribute("teachers", activityService.getActivityTeachersMap());
            return (course.getId() == 0) ? "admin/register-course" : "redirect:/admin/courses/course?id=" + course.getId();

        } else {
            Course returnedCourse = null;
            System.out.println(course);
            returnedCourse = courseService.save(course);
            return "redirect:/admin/courses/course?id=" + returnedCourse.getId();
        }


    }

    /**
     * Process the get request of delete operation
     *
     * @param id            the course id to be deleted
     * @param redirectAttrs to send message of success or failure of the operation
     * @return redirect to the same page of courses list with new info and delete operation result message
     */
    @GetMapping("/admin/courses/course/delete")
    public String deleteCourse(@RequestParam(value = "id") int id, RedirectAttributes redirectAttrs) {
        courseService.delete(id);
        redirectAttrs.addFlashAttribute("message", "Course with ID( " + id + " ) was deleted successfully");
        return "redirect:/admin/courses";
    }

    /**
     * Create the page of details of a specific course
     *
     * @param id the id of required course to get its details
     * @return model of the page with its details of the course.
     */
    private ModelAndView getCourse(int id) {
        ModelAndView modelAndView = new ModelAndView("admin/course-profile");
        Course course = courseService.findById(id);
        Set<Student> students = courseService.getStudentsByCourse(course);
        Map<String, Map<String, Long>> countryStudents = courseService.getCountryCodeStudentsStatusCountMap(course);
        long totalStudents = courseService.totalStudentsByCourse(id);
        long totalStudying = courseService.totalStudentsByStatus(id, StudentStatus.STUDYING);
        long totalWaiting = courseService.totalStudentsByStatus(id, StudentStatus.WAITING);
        long totalCertified = courseService.totalStudentsByStatus(id, StudentStatus.CERTIFIED);
        long totalFired = courseService.totalStudentsByStatus(id, StudentStatus.FIRED);
        long totalTempStopped = courseService.totalStudentsByStatus(id, StudentStatus.TEMP_STOP);
        long totalFinalStopped = courseService.totalStudentsByStatus(id, StudentStatus.FINAL_STOP);

        modelAndView.addObject("course", course);
        modelAndView.addObject("mapCounts", countryService.getCountryCodeStudentsCountMapFromStudetns(students));
        modelAndView.addObject("countryStudents", countryStudents);
        modelAndView.addObject("studyingStudents", courseService.getStudentsByStatus(course, StudentStatus.STUDYING));
        modelAndView.addObject("waitingStudents", courseService.getStudentsByStatus(course, StudentStatus.WAITING));
        modelAndView.addObject("actTeachers", activityService.getTeachersByActivity(course.getActivity().getId()));
        modelAndView.addObject("totalStudents", totalStudents);
        modelAndView.addObject("totalStudying", totalStudying);
        modelAndView.addObject("totalWaiting", totalWaiting);
        modelAndView.addObject("totalCertified", totalCertified);
        modelAndView.addObject("totalFired", totalFired);
        modelAndView.addObject("totalTempStopped", totalTempStopped);
        modelAndView.addObject("totalFinalStopped", totalFinalStopped);

        return modelAndView;
    }

    /**
     * Create model for add course page
     *
     * @return model of page with its attributes
     */
    private ModelAndView addCourse() {
        ModelAndView model = new ModelAndView("admin/register-course");

        model.addObject("course", new Course());
        model.addObject("activities", activityService.findAllOrderByName());
        model.addObject("teachers", activityService.getActivityTeachersMap());
        return model;
    }


}
