from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
from selenium import webdriver as wb
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

app = FastAPI()

class Song(BaseModel):
    songId: int
    name: str
    artists: List[str]
    albumName: str

class SongRequest(BaseModel):
    songs: List[Song]

#json request 요청양식은 이쪽에서 정의
@app.post("/getYoutubeUrls")
async def get_youtube_urls(request: SongRequest):
    driver = wb.Chrome()
    results = []
    for song in request.songs:
        song_id = song.songId
        name = song.name
        artists = song.artists
        album_name = song.albumName
        video_url = None

        # YouTube 검색 URL 생성
        search_query = f"{name}+{'+'.join(artists)}+{'official'}+{'MV'}"
        # encoded_search_query = urllib.parse.quote(search_query)
        youtube_search_url = (
            f"https://www.youtube.com/results?search_query={search_query}"
        )
        driver.get(youtube_search_url)
        wait = WebDriverWait(driver, 10)
        video_url = wait.until(EC.presence_of_element_located((By.CSS_SELECTOR, "ytd-video-renderer a#thumbnail"))).get_attribute("href")

        # 결과에 추가
        results.append({
            "songId": song_id,
            "name": name,
            "artists": artists,
            "albumName": album_name,
            "youtubeUrl": video_url if video_url else "URL not found"
        })
    return {"results": results}

