# 베이스 이미지로 Python 3.11 사용
FROM python:3.11-slim

WORKDIR /app
RUN apt-get -y update && \
    apt install wget && \
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt -y install ./google-chrome-stable_current_amd64.deb


# 작업 디렉토리 설정
WORKDIR /app

# 파이썬 종속성 복사 및 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 애플리케이션 코드 복사
COPY . .

# FastAPI 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
