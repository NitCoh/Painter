# Painter
Main Features:
1) Bounding the number of shapes a user can draw each day (every day it refreshes)
2) Only the user who created the board will be able to delete shapes
3) Each user is registered through his local IP.
4) Real time updating for each user in the entry point
5) Each shape is saved in the DB


Configure server IP:
1) toggle to board.html
2) change to host server IP all the AJAX requests.

Configure DB:
1) Enter path : Painter/initial/src/main/resources/application.properties
2) Change to your relational database (Add URL, user and password) 
3) Start by creating the board : http://PUTHEREYOURLOCALIP:8080/painter/createBoard
4) Toggle to http://PUTHEREYOURLOCALIP:8080/painter/board

Based on : Java, HTML5, JavaScript.
