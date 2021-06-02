package com.example.parkingappv2;

public final class Constants {
    //public static final String BaseUrl = "http://192.168.0.101/parking/public/api/";
    public static final String BaseUrl = "http://192.168.0.104/parking/public/api/";
    public static String bearerToken =
"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNGU0ZGUwYTI5YjVjYzg2Zjk0ZDU1Yzc1NjZkOTQ5NTJmYTI0ZjUyZGVhMjc1YzRjNDU0MmVkY2M0Y2FlOWFlZTQxMjI3ODE2ZTg0NjIzNjkiLCJpYXQiOiIxNjE4OTM0NzcyLjE1MjgxMyIsIm5iZiI6IjE2MTg5MzQ3NzIuMTUyODE3IiwiZXhwIjoiMTY1MDQ3MDc3Mi4wMTYwODgiLCJzdWIiOiIyMSIsInNjb3BlcyI6W119.j0WdLKKXHHM5PRgLBeLIfrR9YNEFJNhLsF5xvjeMiMiKjfFdvOECSFsS_lJpJTdtsxx9okKWQO7IgcXPJ7-4r72FQpDM-mlqQWsDhzSR73Ixf0B8o8RxnHHVrRuewyqfES0egKXeqq4hjz4onBJOwEWIyNM2B52y31i3IV6ZTKsw6c7kZKbppaQycP2IjJgJpfsIE5Tx9Ufx9IfE1CBrTkwlNXNJu5v0_21bnzAQvmOmgcd1gC-zc9FRFnojKIYrN-Ts3clVWiT1F-TWUlhjNoVP9nL7Vkhf0Q_wFLhH5lnoV1KYK0r5cWCIh1QXPsOFWwlerUCj9yUik5WTfy9vk8yiohdJZTtYp1a6worSzmbvUedgNElBE_5sqIxUReWg2T6IdSzeQ7zuNiZf2SunRDPHH_GpqcnHWczmTw9t72vX8B0qfZn88tFIE1008h3cqr6d1j1cHyQEB-vZJ1vuQvZTl6rfhd5j7fhtYdJAHndeGNcw7ewK3PMzncBapX0Z7GmDknGz4K18nO7r0xO8Y4Yl46tHdxSpYtleJt-N-ITasalTPg_34zFsN_VXIct4BqyCI54_3fww01UyFPUy_8FkJiK_6xYde683P1ead934HRy4W3VjPpU-_6CRHA2At8AUQPqwFF0lmeUSeK8BrfrwkWY8ECf2h2mBIQsgAmc";
    public static String user_id;
    public static boolean claimed=false;
    public static int[] v;

    public static final String message1="Our app will take GPS data from your current location, so you must be close to the parking place in order to mark it correctly";
    public static final String message2=" PLEASE fill the ParkingSpot number and press the save button.";
    public static final String thanks_message=" Thank you!";

    public static final String error_availability="Something went wrong, try again or check it later";
    public static final String success_availability="Your ParkingSpot is now available!";

    public static final String error_login="Login failed, try again or check it later";
    public static final String success_login="Login successful";

    public static final String success_showparking="Login failed, try again or check it later";
    public static final String fail_showparking="Something went wrong, try again or check it later";
    public static final String empty_showparking="There are no ParkingSpots attached to your account";

    public static final String fail_general="Something went wrong, try again or check it later";
    public static final String user_deleted="Account deleted successfully";

    public static final String success_phone_update="Phone Number was updated successfully!";
    public static final String error_phone_update="Invalid Phone Number!";

    public static final String success_email_update="Email was updated successfully!";
    public static final String error_email_update="Invalid Email!";
    //sql database sharedpreferences
}
