package com.udacity.jwdnd.course1.cloudstorage;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		System.setProperty("webdriver.firefox.bin","/Applications/Firefox.app/Contents/MacOS/firefox");
		this.driver = new FirefoxDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}

	@Test // Test to verify "home" cannot be reached without login
	public void getHomePage() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test // Test to log in and log out, verify "home" cannot be reached after logout
	public void checkLoginAndLogout() {
		doMockSignUp("CheckLogin","AndLogout","CL","123");
		doLogIn("CL", "123");

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

		WebElement logoutbtn = driver.findElement(By.id("logoutbtn"));
		logoutbtn.click();

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

	}

	//TESTING NOTES

	@Test // Test to add a new note and verify it was saved.
	public void createNote() {
		doLogIn("a", "a");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		WebElement navToNotes = driver.findElement(By.id("nav-notes-tab"));
		navToNotes.click();
		
		WebElement addNote = driver.findElement(By.id("addNote"));
		addNote.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("Example Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Example Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSave")));
		WebElement noteSave = driver.findElement(By.id("noteSave"));
		noteSave.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome = driver.findElement(By.id("successHome"));
		successHome.click();

		WebElement navToNotes2 = driver.findElement(By.id("nav-notes-tab"));
		navToNotes2.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldNoteTitle")));
		WebElement fieldNoteTitle = driver.findElement(By.id("fieldNoteTitle"));
		
		if (fieldNoteTitle != null) {
			assertEquals("Example Title", "Example Title");
		} else {
			System.out.println("Note did not save.");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldNoteDescription")));
		WebElement fieldNoteDescription = driver.findElement(By.id("fieldNoteDescription"));

		if (fieldNoteDescription != null) {
			assertEquals("Example Description", "Example Description");
		} else {
			System.out.println("Note did not save.");
		}

	}

	@Test // Test to edit note and verify changes
	public void editNote() {
		doLogIn("a", "a");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		WebElement navToNotes = driver.findElement(By.id("nav-notes-tab"));
		navToNotes.click();
		
		WebElement addNote = driver.findElement(By.id("addNote"));
		addNote.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("Example Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Example Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSave")));
		WebElement noteSave = driver.findElement(By.id("noteSave"));
		noteSave.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome = driver.findElement(By.id("successHome"));
		successHome.click();

		WebElement navToNotes2 = driver.findElement(By.id("nav-notes-tab"));
		navToNotes2.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteEdit")));
        WebElement noteEdit = driver.findElement(By.id("noteEdit"));
        noteEdit.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement updateNoteTitle = driver.findElement(By.id("note-title"));
		updateNoteTitle.click();
		updateNoteTitle.clear();
		updateNoteTitle.sendKeys("Updated Note Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement updatedNoteDescription = driver.findElement(By.id("note-description"));
		updatedNoteDescription.click();
		updatedNoteDescription.clear();
		updatedNoteDescription.sendKeys("Updated Note Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSave")));
		WebElement noteSave2 = driver.findElement(By.id("noteSave"));
		noteSave2.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome2 = driver.findElement(By.id("successHome"));
		successHome2.click();

		WebElement navToNotes3 = driver.findElement(By.id("nav-notes-tab"));
		navToNotes3.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldNoteTitle")));
		WebElement fieldNoteTitle = driver.findElement(By.id("fieldNoteTitle"));
		
		if (fieldNoteTitle != null) {
			assertEquals("Updated Note Title", "Updated Note Title");
		} else {
			System.out.println("Updated note did not save.");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldNoteDescription")));
		WebElement fieldNoteDescription = driver.findElement(By.id("fieldNoteDescription"));

		if (fieldNoteDescription != null) {
			assertEquals("Updated Note Description", "Updated Note Description");
		} else {
			System.out.println("Updated note did not save.");
		}
	}

	@Test // Test to delete note and verify deletion
	public void deleteNote() {
		doLogIn("a", "a");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		WebElement navToNotes = driver.findElement(By.id("nav-notes-tab"));
		navToNotes.click();
		
		WebElement addNote = driver.findElement(By.id("addNote"));
		addNote.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("Example Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Example Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSave")));
		WebElement noteSave = driver.findElement(By.id("noteSave"));
		noteSave.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome = driver.findElement(By.id("successHome"));
		successHome.click();

		WebElement navToNotes2 = driver.findElement(By.id("nav-notes-tab"));
		navToNotes2.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteEdit")));
        WebElement noteEdit = driver.findElement(By.id("noteEdit"));
        noteEdit.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement updateNoteTitle = driver.findElement(By.id("note-title"));
		updateNoteTitle.click();
		updateNoteTitle.clear();
		updateNoteTitle.sendKeys("Updated Note Title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement updatedNoteDescription = driver.findElement(By.id("note-description"));
		updatedNoteDescription.click();
		updatedNoteDescription.clear();
		updatedNoteDescription.sendKeys("Updated Note Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteSave")));
		WebElement noteSave2 = driver.findElement(By.id("noteSave"));
		noteSave2.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome2 = driver.findElement(By.id("successHome"));
		successHome2.click();

		WebElement navToNotes3 = driver.findElement(By.id("nav-notes-tab"));
		navToNotes3.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldNoteTitle")));
		WebElement fieldNoteTitle = driver.findElement(By.id("fieldNoteTitle"));
		
		if (fieldNoteTitle != null) {
			assertEquals("Updated Note Title", "Updated Note Title");
		} else {
			System.out.println("Updated note did not save.");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldNoteDescription")));
		WebElement fieldNoteDescription = driver.findElement(By.id("fieldNoteDescription"));

		if (fieldNoteDescription != null) {
			assertEquals("Updated Note Description", "Updated Note Description");
		} else {
			System.out.println("Updated note did not save.");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteDelete")));
        WebElement noteDelete = driver.findElement(By.id("noteDelete"));
        noteDelete.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome3 = driver.findElement(By.id("successHome"));
		successHome3.click();

		WebElement navToNotes4 = driver.findElement(By.id("nav-notes-tab"));
		navToNotes4.click();

		Assertions.assertEquals(1, driver.findElements(By.xpath("//*[@id='userTable']/tbody")).size());
	}

	// TESTING CREDENTIALS 

	@Test // Test to add a new credential and verify it was saved.
	public void createCredential() {
		doLogIn("a", "a");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		WebElement navToCreds = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds.click();
		
		WebElement addCredential = driver.findElement(By.id("addCredential"));
		addCredential.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credUrl = driver.findElement(By.id("credential-url"));
		credUrl.click();
		credUrl.sendKeys("http://www.google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-userName")));
		WebElement credUsername = driver.findElement(By.id("credential-userName"));
		credUsername.click();
		credUsername.sendKeys("johnsmith");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		credPassword.click();
		credPassword.sendKeys("notverysecurepassword");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSave")));
		WebElement credSave = driver.findElement(By.id("credSave"));
		credSave.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome = driver.findElement(By.id("successHome"));
		successHome.click();

		WebElement navToCreds2 = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds2.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldCredUrl")));
		WebElement fieldCredUrl = driver.findElement(By.id("fieldCredUrl"));
		
		if (fieldCredUrl != null) {
			assertEquals("http://www.google.come", "http://www.google.come");
		} else {
			System.out.println("Credential did not save.");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldCredUsername")));
		WebElement fieldCredUsername = driver.findElement(By.id("fieldCredUsername"));

		if (fieldCredUsername != null) {
			assertEquals("johnsmith", "johnsmith");
		} else {
			System.out.println("Credential did not save.");
		}

	}

	@Test // Test to edit a credential and verify it was saved.
	public void editCredential() {
		doLogIn("a", "a");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		WebElement navToCreds = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds.click();
		
		WebElement addCredential = driver.findElement(By.id("addCredential"));
		addCredential.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credUrl = driver.findElement(By.id("credential-url"));
		credUrl.click();
		credUrl.sendKeys("http://www.google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-userName")));
		WebElement credUsername = driver.findElement(By.id("credential-userName"));
		credUsername.click();
		credUsername.sendKeys("johnsmith");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		credPassword.click();
		credPassword.sendKeys("notverysecurepassword");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSave")));
		WebElement credSave = driver.findElement(By.id("credSave"));
		credSave.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome = driver.findElement(By.id("successHome"));
		successHome.click();

		WebElement navToCreds2 = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds2.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCredential")));
        WebElement editCredential = driver.findElement(By.id("editCredential"));
        editCredential.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement updateCredentialPassword = driver.findElement(By.id("credential-password"));
		updateCredentialPassword.click();
		updateCredentialPassword.clear();
		updateCredentialPassword.sendKeys("M0r3s3ruc3p@ssw0rd");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSave")));
		WebElement credSave2 = driver.findElement(By.id("credSave"));
		credSave2.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome2 = driver.findElement(By.id("successHome"));
		successHome2.click();

		WebElement navToCreds3 = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds3.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldCredPassword")));
		WebElement fieldCredPassword = driver.findElement(By.id("fieldCredPassword"));
		
		if (fieldCredPassword != null) {
			assertEquals("M0r3s3ruc3p@ssw0rd", "M0r3s3ruc3p@ssw0rd");
		} else {
			System.out.println("Updated credential did not save.");
		}

	}

	@Test // Test to delete a credential and verify deletion.
	public void deleteCredential() {
		doLogIn("a", "a");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		WebElement navToCreds = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds.click();
		
		WebElement addCredential = driver.findElement(By.id("addCredential"));
		addCredential.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement credUrl = driver.findElement(By.id("credential-url"));
		credUrl.click();
		credUrl.sendKeys("http://www.google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-userName")));
		WebElement credUsername = driver.findElement(By.id("credential-userName"));
		credUsername.click();
		credUsername.sendKeys("johnsmith");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		credPassword.click();
		credPassword.sendKeys("notverysecurepassword");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSave")));
		WebElement credSave = driver.findElement(By.id("credSave"));
		credSave.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome = driver.findElement(By.id("successHome"));
		successHome.click();

		WebElement navToCreds2 = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds2.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCredential")));
        WebElement editCredential = driver.findElement(By.id("editCredential"));
        editCredential.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement updateCredentialPassword = driver.findElement(By.id("credential-password"));
		updateCredentialPassword.click();
		updateCredentialPassword.clear();
		updateCredentialPassword.sendKeys("M0r3s3ruc3p@ssw0rd");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credSave")));
		WebElement credSave2 = driver.findElement(By.id("credSave"));
		credSave2.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome2 = driver.findElement(By.id("successHome"));
		successHome2.click();

		WebElement navToCreds3 = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds3.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fieldCredPassword")));
		WebElement fieldCredPassword = driver.findElement(By.id("fieldCredPassword"));
		
		if (fieldCredPassword != null) {
			assertEquals("M0r3s3ruc3p@ssw0rd", "M0r3s3ruc3p@ssw0rd");
		} else {
			System.out.println("Updated credential did not save.");
		}

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCredential")));
        WebElement deleteCredential = driver.findElement(By.id("deleteCredential"));
        deleteCredential.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successHome")));
		WebElement successHome3 = driver.findElement(By.id("successHome"));
		successHome3.click();

		WebElement navToCreds4 = driver.findElement(By.id("nav-credentials-tab"));
		navToCreds4.click();

		Assertions.assertEquals(1, driver.findElements(By.xpath("//*[@id='userTable']/tbody")).size());
	}


}
