package pageObjects;

import org.openqa.selenium.*;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;

public class LoginPage 
{
	private static WebElement element = null;
	public static WebElement username(WebDriver driver)
	{
		element = driver.findElement(By.id("edit_text_email"));
		return element;
	}
	public static WebElement password(WebDriver driver)
	{
		element = driver.findElement(By.id("edit_text_password"));
		return element;
	}

	public static WebElement login(WebDriver driver)
	{
		element = driver.findElement(By.name("Log in"));
		return element;
	}
}