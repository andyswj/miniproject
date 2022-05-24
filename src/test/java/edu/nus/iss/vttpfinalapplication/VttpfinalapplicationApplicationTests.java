package edu.nus.iss.vttpfinalapplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.iss.vttpfinalapplication.controllers.LoginController;
import edu.nus.iss.vttpfinalapplication.controllers.ProtectedController;
import edu.nus.iss.vttpfinalapplication.models.Review;
import edu.nus.iss.vttpfinalapplication.repositories.ReviewRepository;
import edu.nus.iss.vttpfinalapplication.repositories.UserRepository;
import edu.nus.iss.vttpfinalapplication.services.LoginService;
import edu.nus.iss.vttpfinalapplication.services.ModuleService;
import edu.nus.iss.vttpfinalapplication.services.ReviewService;

// @AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VttpfinalapplicationApplicationTests {

	// @Autowired
	// private MockMvc mockMvc;

	@Autowired 
	private LoginService loginSvc;

	@Autowired
	private ModuleService moduleSvc;

	@Autowired
	private ReviewService reviewSvc;

	@Autowired 
	ReviewRepository reviewRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	private LoginController loginCon;

	@Autowired
	private ProtectedController protectCon;

	@Mock
	private Model model;

	@Mock
	private HttpSession sess;

	@Test
	@Order(1)
	void shouldNotLogin() {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", "1234");
		map.add("password", "test");

		HttpSession session = null;

		ModelAndView mav = loginCon.loginAuthetication(map, session);
		assertTrue(mav.getViewName() == "error");
	}
	
	@Test
	@Order(2)
	public void checkUser() {
		String username = "test";
		String hashPassword =  loginSvc.hashingPassword("test");
		assertFalse(loginSvc.autheticationWithDB(username, hashPassword));
	}

	@Test
	@Order(3)
	public void createUser() {
		String username = "test";
		String hashPassword =  loginSvc.hashingPassword("test");
		assertEquals(1, loginSvc.createUserWithDB(username, hashPassword));
	}

	@Test
	@Order(4)
	public void userCannotBeCreated() {
		String username = "test";
		String hashPassword =  loginSvc.hashingPassword("test");
		assertEquals(0, loginSvc.createUserWithDB(username, hashPassword));
	}

	@Test
	@Order(5)
	public void userExist() {
		String username = "test";
		String hashPassword =  loginSvc.hashingPassword("test");
		assertTrue(loginSvc.autheticationWithDB(username, hashPassword));
	}

	@Test
	@Order(6)
	void shouldLogin() {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", "test");
		map.add("password", "test");

		HttpSession session = mock(HttpSession.class);

		ModelAndView mav = loginCon.loginAuthetication(map, session);
		assertTrue(mav.getViewName() == "redirect:/protected/review");
	}

	@Test
	@Order(7)
	void shouldCreateUser() {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", "test2");
		map.add("password", "test2");

		HttpSession session = mock(HttpSession.class);

		ModelAndView mav = loginCon.createAccount(map, session);
		// System.out.println("viewname1: " + mav.getViewName());
		assertTrue(mav.getViewName() == "redirect:/protected/review");
	}

	@Test
	@Order(8)
	void shouldNotCreateUser() {

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", "test2");
		map.add("password", "test2");

		HttpSession session = mock(HttpSession.class);

		ModelAndView mav = loginCon.createAccount(map, session);
		// System.out.println("viewname2: " + mav.getViewName());
		assertTrue(mav.getViewName() == "error");
	}

	@Test
	@Order(9)
	public void getAPImodule() {
		assertNotNull(moduleSvc.getISSModule());
	}

	@Test
	@Order(10)
	public void createReview() {
		Review review = new Review();
		review.setModuleName("Digital Organisation Models");
		review.setComment("I had a great time learning this module");
		review.setRating(4);

		assertEquals(1, reviewSvc.addReview(review, "test"));
	}

	@Test
	@Order(11)
	public void deleteReview() {

		Page<Review> reviewPage = reviewSvc.findPaginatedByUserId(PageRequest.of(1 - 1, 100), "test");

		// for(Review review : reviewPage.getContent()){
		// 	System.out.println("Review: "+ review.getComment());
		// }

		int sizeBefore = reviewPage.getContent().size();
		// System.out.println("before:" + sizeBefore);

		int userId = (Integer) userRepo.selectUserIdByUsername("test");

		// System.out.println("id " + Integer.toString(userId));

		List<Review> reviews = reviewRepo.getReviewByUserId(userId);

		// System.out.println("Count: " + reviews.size());

		for(Review review : reviews) {
			// System.out.println("Review" + review.getComment());
			reviewSvc.deleteReview(review.getReviewId(), "test");
		}

		Page<Review> reviewPage2 = reviewSvc.findPaginatedByUserId(PageRequest.of(1 - 1, 100), "test");
		
		int sizeAfter = reviewPage2.getContent().size();
		// System.out.println("after:" + sizeAfter);

		assertEquals(0, sizeAfter);
	}

	@Test
	@Order(12)
	void checkPagination() {
		
		Page<Review> reviewPage = reviewSvc.findPaginated(PageRequest.of(1 - 1, 100), "Digital Organisation Models");

		int size = reviewPage.getContent().size();

		// for(Review review : reviewPage.getContent()){
		// 	System.out.println("Review: "+ review.getComment());
		// }

		assertEquals(3, size);
	}

	@Test
	@Order(13)
	void checkAllModuleListed() {

		String result = loginCon.listAllReview("Digital Organisation Models", Optional.of(1), model);
		assertTrue(result == "index");
	}

	@Test
	@Order(14)
	void accessToReviewHome() {

		sess.setAttribute("username", "test2");
		
		// System.out.println("username0: " + sess.getAttribute("username"));

		String result = protectCon.home(model, sess, Optional.of(1));
		assertTrue(result == "review");
	}

}
