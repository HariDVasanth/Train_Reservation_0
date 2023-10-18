# Train_Reservation_0
This is a Java applicaition for train reservation and management. It 
allows users to perform various operations related to train booking, 
cancellation, and registration. Here's a summary of what this program 
does.
1. It defines a details class responsible for handling train details, booking, and 
cancellation.
2. Users can log in as either employees or regular users. 
3. Employees have additional privileges, such as adding new train details.
4. Regular users can book and cancel tickets.
5. The eLogin method allows employees to log in, add new train details to the 
database, and specify details like train name, number, starting point, 
destination, available seats, sleeper seats, AC seats, sleeper fare, and AC fare.
6. The login method allows registered users to log in. After logging in, users can 
perform operations like booking or canceling tickets.
7. The book method allows users to choose from options like displaying all 
available trains, searching by starting point, and booking tickets. Users can 
specify the train, seat type (AC or Sleeper), and the number of seats to book. 
8. The cancel method allows users to cancel their booked tickets, specifying the 
train name, seat type, and email address for idenification.
9. The register method lets users create a new account by providing a username 
and a strong password. A strong password is defined as containing at least one 
number, one uppercase letter, and having a minimum length of eight 
characters. 
10. The program uses a MySQL database to store information about trains, user 
registration details, and ticket bookings.
11. The program also uses JavaMail to send emails to users when tickets are 
booked or canceled and when seats become available from the waiting list.
12. The program automatically sends mail to the users in the waiting list when a 
ticket is cancelled.
13. All the process in this application is dynamically updated in the database.

