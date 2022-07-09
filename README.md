# PasswordKeeper

## Idea

The basic idea is to keep all passwords in a custom password manager

## MVP Scope

As a user, I want to C.R.U.D. passwords. All passwords must be securely encrypted.

## How it works

Based on MVP Scope, we can specify next behaviors:

* User can create a new entry using a template (name, password)
* User can see already saved passwords
* User can update already saved passwords
* User can delete password by its ID
* User can create a master password to enter the program
* User can change a master-password if needed
* User can select an external file in which all encrypted passwords will be saved, otherwise it will be created
  next to the program

## Technological stack

* SpringBoot as a skeleton framework
* JavaFX Weaver Starter
* JavaFX for GUI

## License

This project is Apache License 2.0 - see
the [LICENSE](https://github.com/VladyslavBabenko/PasswordKeeper/blob/master/LICENSE) file for details