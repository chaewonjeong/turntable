from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List

from selenium import webdriver as wb
from selenium.webdriver.common.by import By
import time
from bs4 import BeautifulSoup
import requests
import urllib.parse

driver = wb.Chrome()

app = FastAPI()

# youtube data api key
# AIzaSyBd-ELfGyhe8pTI8HF9k2vU6vdVsJ9tCGE
class Song(BaseModel):
    name: str
    artists: List[str]
    albumName: str

class SongRequest(BaseModel):
    songs: List[Song]


#json request 요청양식은 이쪽에서 정의
@app.post("/getYoutubeUrls")
async def get_youtube_urls(request: SongRequest):
    results = []
    for song in request.songs:
        name = song.name
        artists = song.artists
        album_name = song.albumName
        video_url = None

        # YouTube 검색 URL 생성
        search_query = f"{name}|{'|'.join(artists)}|{album_name}"
        encoded_search_query = urllib.parse.quote(search_query)
        youtube_search_url = (
            f"https://www.youtube.com/results?search_query={encoded_search_query}"
        )
        driver.get(youtube_search_url)
        video_url = driver.find_element(By.CSS_SELECTOR, "div#dismissible.style-scope.ytd-video-renderer > ytd-thumbnail > a#thumbnail").get_attribute("href")

        # 결과에 추가
        results.append({
            "name": name,
            "artists": artists,
            "albumName": album_name,
            "youtubeUrl": video_url if video_url else "URL not found"
        })

    return {"results": results}

