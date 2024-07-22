FROM --platform=linux/amd64 python:3.9
WORKDIR /app
RUN apt-get -y update && \
    apt install wget && \
    wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
    apt -y install ./google-chrome-stable_current_amd64.deb   # Google Chrome 다운로드 및 설치


# Copy the requirements.txt file into the working directory
COPY requirements.txt .
# Install the dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code into the working directory
COPY ./app ./app

# Expose the port FastAPI will run on
EXPOSE 8000

# Command to run the FastAPI server
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
