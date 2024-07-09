<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Objects" %>
<%
    String username = (String) session.getAttribute("username");
    Long userId = (Long) session.getAttribute("userId");
    String pageOwner = request.getParameter("username");
    String pageOwnerIdParam = request.getParameter("pageOwnerId"); // URL 파라미터로 페이지 소유자 ID를 받음
    Long pageOwnerId = null;

    if (pageOwnerIdParam != null && !pageOwnerIdParam.isEmpty()) {
        try {
            pageOwnerId = Long.parseLong(pageOwnerIdParam);
        } catch (NumberFormatException e) {
            pageOwnerId = userId; // 예외 발생 시 기본값으로 설정
        }
    } else {
        pageOwnerId = userId; // 파라미터가 없을 때 기본값으로 설정
    }

    if (pageOwner == null) {
        pageOwner = username;
    }

    boolean isOwner = Objects.equals(userId, pageOwnerId); // 현재 로그인한 사용자와 페이지 소유자가 같은지 확인
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Background Image</title>
    <link rel="stylesheet" href="/css/global.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="header">
    <div class="top-bar">
        <i class="fas fa-user user-icon" id ="users-icon"></i>
        <i class="fas fa-home home-icon" id="home-icon"></i>
        <i class="fa-solid fa-compact-disc" id="settings-icon"></i>
    </div>
</div>
<script>
  $(function(){
    const userId = "<%= userId %>";

      document.getElementById("home-icon").addEventListener("click", function() {
        window.location.href = "/main?pageOwnerId="+userId;
      });
      document.getElementById("users-icon").addEventListener("click", function() {
        window.location.href = "/users";
      });
      document.getElementById("settings-icon").addEventListener("click", function() {
        document.getElementById("settings-drawer").classList.toggle("open");
      });
    });

  $("#footer-placeholder").load("footer.html");

  $(document).ready(function() {
    const username = "<%= username %>";
    const pageOwnerId = "<%= pageOwnerId %>";
    console.log(username,pageOwnerId);

    fetch('/imgurl?pageOwnerId='+pageOwnerId, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.text();
    })
    .then(imageUrl => {
      console.log("Image URL:", imageUrl); // URL을 로그로 출력
      document.body.style.backgroundImage = "url('" + imageUrl + "')";
      document.body.style.backgroundSize = "cover";
    })
    .catch(error => console.error('Error fetching image URL:', error));
  });
</script>
</body>
</html>
