from typing import Union

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Dict

app = FastAPI()


class Song(BaseModel):
    name: str
    artists: List[str]
    albumName: str

class SongRequest(BaseModel):
    songs: List[Song]


@app.post("/getYoutubeUrls")
async def get_youtube_urls(request: SongRequest):
    results = []
    for song in request.songs:
        results.append({
            "name": song.name,
            "artists": song.artists,
            "albumName": song.albumName,
            "youtubeUrl": "test"
        })
    return {"results": results}

