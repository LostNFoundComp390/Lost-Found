//Checks the textboxes for incorrect entries
package com.lostandfound.services;
import com.lostandfound.db.Database;
import com.lostandfound.repo.DbUserRepository;
import com.lostandfound.ui.ControllerFrame;
import com.lostandfound.ui.CreateAccount;


public class FieldChecks
{

    Database database = Database.production();
    DbUserRepository userRepo = new DbUserRepository(database);

    private String enteredUsername= "";
    private String enteredPassword = "";
    private String confirmedPassword = "";
    private String enteredEmail = "";
    private String enteredFirstName = "";
    private String eneteredLastName = "";

    //Checks to see if usernameField is empty
    public boolean isUsernameFieldEmpty(String username)
    {
        enteredUsername = username;
        if(enteredUsername.isBlank())
            return true;
        else
            return false;
    }//end isUsernameFieldEmpty

    //Checks to see if the username is in the database
    public boolean isUsernameAvailable(String username)
    {
        enteredUsername = username;
        if(userRepo.exists(enteredUsername))
            return false;
        else
            return true;
    }//end isUsernameAvailable

    //Checks to see if the username meets length requirement
    public boolean isUsernameMinimumLength(String username)
    {
        enteredUsername = username;
        if(enteredUsername.length() > 4)
            return true;
        else
            return false;
    }//end isUsernameMinimumLength

    //Checks to see if the password field is empty
    public boolean isPasswordFieldEmpty(String password)
    {
        enteredPassword = password;
        if(enteredPassword.isBlank())
            return true;
        else
            return false;
    }//end isPasswordFieldEmpty

    //Checks to see if password and confirmedPassword match
    public boolean doPasswordsMatch(String password, String reenteredPassword) {

        enteredPassword = password;
        confirmedPassword = reenteredPassword;

        return enteredPassword.equals(confirmedPassword);
    }//end doPasswordsMatch

    //Checks to see if password contains illegal characters
    public boolean isPasswordLegal(String password)
    {
        boolean isLegal = true;
        enteredPassword = password;

        for (int i = 0; i < password.length(); ++i)
        {
            if (password.charAt(i) == ' ')
                isLegal = false;

        }
        return isLegal;
    }//end isPasswordLegal

    public boolean passwordLength(String password)
    {
        boolean isLongEnough = true;
        enteredPassword = password;
        if(enteredPassword.length() < 8)
            isLongEnough = false;

        return isLongEnough;
    }

    //checks to see if email text field is empty
    public boolean isEmailTextFieldEmpty(String email)
    {
        enteredEmail = email;
        if(enteredEmail.isBlank())
            return true;
        else
            return false;
    }//end isEmailTextFieldEmpty

    //checks to see if email is connected to a username
    //public boolean emailConnectedToUsername

//    public boolean isEmailSyntaxCorrect(String email)
//    {
//
//        enteredEmail = email;
//        boolean isEmailSyntaxRight = false;
//        int lengthOfEmail = enteredEmail.length();
//
//        if(enteredEmail.substring((lengthOfEmail - 19), (lengthOfEmail - 1)).equals("@student.bridgew.edu"))
//            isEmailSyntaxRight = true;
//        else
//            isEmailSyntaxRight = false;
//
//        return isEmailSyntaxRight;

//    }
    //Checks to see if First Name textbox is empty
    public boolean isFirstNameFieldEmpty(String firstName)
    {
        enteredFirstName = firstName;
        if(enteredFirstName.isBlank())
            return true;
        else
            return false;
    }//end isFirstNameFieldEmpty

    //checks to see if last name textbox is empty
    public boolean isLastNameFieldEmpty(String lastName)
    {
        eneteredLastName = lastName;
        if(eneteredLastName.isBlank())
            return true;
        else
            return false;

    }//end isLastNameFieldEmpty

}//end FieldChecks






