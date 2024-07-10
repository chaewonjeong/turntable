from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
from bs4 import BeautifulSoup
from youtube_config import key
import requests
import urllib.parse


app = FastAPI()

# youtube data api key
# AIzaSyBd-ELfGyhe8pTI8HF9k2vU6vdVsJ9tCGE
class Song(BaseModel):
    name: str
    artists: List[str]
    albumName: str

class SongRequest(BaseModel):
    songs: List[Song]


youtube_api_key = key


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
            f"https://www.googleapis.com/youtube/v3/search?part=id&topicId=/m/04rlf&q={encoded_search_query}"
            f"&maxResults=1&type=video&videoLicense=youtube&videoEmbeddable=true&key={youtube_api_key}"
        )
        print(youtube_search_url)

        # YouTube 검색 결과 페이지 가져오기
        response = requests.get(youtube_search_url)
        if response.status_code != 200:
            raise HTTPException(status_code=500, detail="YouTube 검색 실패")

        # JSON 응답 데이터 파싱
        data = response.json()

        # videoId 추출
        if "items" in data and len(data["items"]) > 0:
            video_id = data["items"][0]["id"]["videoId"]
            video_url = f"https://www.youtube.com/watch?v={video_id}"
            print("Video ID:", video_id)
        else:
            print("No video ID found")

        # 결과에 추가
        results.append({
            "name": name,
            "artists": artists,
            "albumName": album_name,
            "youtubeUrl": video_url if video_url else "URL not found"
        })

    return {"results": results}