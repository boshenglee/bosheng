# tutorials

ALL tutorials for cs309 will be in this repo.
Each topic will be on a different branch. Example: Mockito, WebSockets etc.
Each platform will be a FOLDER in this repo.
GIT wiki will be used for documentation.
Different levels will be in sub branches such as Mockito-1, Mockito-2 etc.

See the WIKI [https://git.linux.iastate.edu/cs309/tutorials/wikis/home](https://git.linux.iastate.edu/cs309/tutorials/wikis/home)

<h1> EMS : Employee Management System</h1></br>

This project illustrates the use of Spring Jpa with one-to-many,
many-to-one and many-to-many relations. It also showcases how messages formatted as JSON
are to be sent to the frontend if required.

The database schema is as follows 

<b>User</b> - The application admin offered by mycompany, and is
not related to any other entity.</br>

<b>Company -</b> The company using the Employee management service</br>

<b>Employee -</b> The Employee of a particular company</br>

<b>EmployeeForum - </b> Forums created within the company for employees to join</br>

<h3>Table Relations</h3>

<ul>
  <li>A Company can have many employees as well as employee-forms and thus is a one-to-many with both Employee and EmployeeForum</li>
  <li>Employee can belong to only one company, but many employees can belong to one company. This is a many to one relation</li>
  <li>Employee can sign up to multiple EmployeeForums, and An EmployeeForum can have multiple employees registered to it. This is a many-to-many relation</li>
  <li>A Company can have multiple Forums, whereas a Forum requires exactly one company to belong to. This is again a many-to-one relation</li>
</ul>

<h3>API's</h3>

<ul>
  <li>User Controller</li>
      <ul>
          <li> PUT request localhost:8080/api/vi/user : Accepts user information, requires all fields to be present except createdOn and ModifiedOn </li>
          <li> POST request localhost:8080/api/vi/user : Accepts user email Id and password and returns current user data </li>
          <li> GET request localhost:8080/api/vi/user : Gets all users present </li>
      </ul>
  <li>EmployeeForum Controller</li>
       <ul>
          <li> GET request localhost:8080/api/v1/company/{c_id}/forums : Accepts company Id as a path variable and lists all the forums </li>
          <li> GET request localhost:8080/api/v1/company/{c_id}/employee/{e_id}/forums : Accepts company id and employee id and gets all the forums for an employee and company </li>
       </ul>
  <li>Test Controller</li>
        <ul>
          <li> GET request localhost:8080/test/enter : enters dummy data so as to allow more functionality and querying </li>
        </ul>
</ul>

<h3>Service</h3>

The service is generally where the logic for the API is present. For example the PUT api for the User Controller accepts a User
as input, But a user with wrong time stamp can be provided directly by the user so as to mess with the database. Or there maybe users 
who are in different time zones. The Logic for checking such stuff should be present in the service.


