# Train_Reservation_0
This is a Java applicaiƟon for train reservaƟon and management. It 
allows users to perform various operaƟons related to train booking, 
cancellaƟon, and registraƟon. Here's a summary of what this program 
does: 
 It defines a details class responsible for handling train details, booking, and 
cancellaƟon.
 Users can log in as either employees or regular users: 
 Employees have addiƟonal privileges, such as adding new train details.
 Regular users can book and cancel Ɵckets.
 The eLogin method allows employees to log in, add new train details to the 
database, and specify details like train name, number, starƟng point, 
desƟnaƟon, available seats, sleeper seats, AC seats, sleeper fare, and AC fare.
 The login method allows registered users to log in. AŌer logging in, users can 
perform operaƟons like booking or canceling Ɵckets.
 The book method allows users to choose from opƟons like displaying all 
available trains, searching by starƟng point, and booking Ɵckets. Users can 
specify the train, seat type (AC or Sleeper), and the number of seats to book. 
 The cancel method allows users to cancel their booked Ɵckets, specifying the 
train name, seat type, and email address for idenƟficaƟon.
 The register method lets users create a new account by providing a username 
and a strong password. A strong password is defined as containing at least one 
number, one uppercase leƩer, and having a minimum length of eight 
characters. 
 The program uses a MySQL database to store informaƟon about trains, user 
registraƟon details, and Ɵcket bookings.
 The program also uses JavaMail to send emails to users when Ɵckets are 
booked or canceled and when seats become available from the waiƟng list.
 The program automaƟcally sends mail to the users in the waiƟng list when a 
Ɵcket is cancelled.
 All the process in this applicaƟon is dynamically updated in the database.
