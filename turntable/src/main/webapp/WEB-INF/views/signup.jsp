<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/signup.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container">
    <div class="left">
        <h1>Turntable; 매일 당신만을 위한 플레이리스트</h1>
        <p>Turntable ; 은 매일 새로운 음악을 발견하고 싶은 당신을 위해</p>
        <p>맞춤형 플레이리스트를 제공합니다.</p>
        <p>당신의 취향과 분위기에 맞춘 신선한 곡들을 추천받아</p>
        <p>음악 감상의 즐거움을 누려보세요.</p>
    </div>
    <div class="right">
        <div class="signup-container">
            <h1 class="text-center">Sign up</h1>
            <form id="signup-form" action="/signup" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="nickname">이름:</label>
                    <input type="text" class="form-control" id="nickname" name="nickname" required>
                </div>
                <div class="form-group">
                    <label for="name">아이디:</label>
                    <div class="input-group">
                        <input type="text" class="form-control" id="name" name="name" required>
                        <div class="input-group-append">
                            <button type="button" class="btn btn-outline-secondary" id="check-username">중복확인</button>
                        </div>
                    </div>
                    <span id="username-message" class="form-text"></span>
                </div>
                <div class="form-group">
                    <label for="password">비밀번호:</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="form-group">
                    <label for="confirm-password">비밀번호 확인:</label>
                    <input type="password" class="form-control" id="confirm-password" name="confirm-password" required>
                    <span id="password-message" class="form-text"></span>
                </div>
                <div class="form-group">
                    <label for="bgImg">배경화면 업로드:</label>
                    <input type="file" class="form-control-file" id="bgImg" name="bgImg" accept="image/*">
                    <span id="file-message" class="form-text"></span>
                </div>
                <button type="submit" class="btn btn-primary btn-block">회원가입</button>
            </form>
        </div>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
  $(document).ready(function() {
    const MAX_FILE_SIZE = 10*1024*1024;

    $('#name').on('input', function() {
      $('#username-message').text('');
    });

    $('#check-username').click(function() {
      const name = $('#name').val();
      $.ajax({
        url: '/check-username',
        method: 'GET',
        data: { name: name },
        success: function(response) {
          if (response.available) {
            $('#username-message').text('사용 가능한 아이디입니다.').css('color', 'green');
          } else {
            $('#username-message').text('이미 사용 중인 아이디입니다.').css('color', 'red');
          }
        },
        error: function() {
          $('#username-message').text('오류가 발생했습니다. 다시 시도해주세요.').css('color', 'red');
        }
      });
    });

    $('#signup-form').submit(function(event) {
      const password = $('#password').val();
      const confirmPassword = $('#confirm-password').val();
      if (password !== confirmPassword) {
        $('#password-message').text('비밀번호가 일치하지 않습니다.').css('color', 'red');
        event.preventDefault();
      }

      const fileInput = $('#bgImg')[0];
      if(fileInput.files.length > 0){
        const file = fileInput.files[0];
        if(file.size > MAX_FILE_SIZE){
          $('#file-message').text("파일 크기가 너무 큽니다. 10MB 이하로 줄여주세요.");
          event.preventDefault();
          return;
        }
      }
    });
  });
</script>
</body>
</html>
