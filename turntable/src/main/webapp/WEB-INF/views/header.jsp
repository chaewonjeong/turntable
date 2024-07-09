<%@ page import="java.util.Objects" %><%
    String sessionUsername = (String) session.getAttribute("username");
    Long sessionUserId = (Long) session.getAttribute("userId");
    Long pageOwnerId = Long.parseLong(request.getParameter("pageOwnerId")); // URL 파라미터로 페이지 소유자 이름을 받음

    boolean isOwner = Objects.equals(sessionUserId, pageOwnerId); // 현재 로그인한 사용자와 페이지 소유자가 같은지 확인
%>
<div class="header">
    <div class="top-bar">
        <i class="fas fa-user user-icon" id ="users-icon"></i>
        <i class="fas fa-home home-icon" id="home-icon"></i>
        <i class="fa-solid fa-compact-disc" id="settings-icon"></i>
    </div>
</div>
<script>
  const username = "<%= sessionUsername %>";
  const userId="<%= sessionUserId %>";
</script>