from fastapi import FastAPI, UploadFile
from pypdf import PdfReader
import re


app = FastAPI()

phone_pe = re.compile(r"(\w{3}\s\d{2},\s\d{4})\s+(?:Paid to|Received from)\s+(.*?)(?=\s{2})\s+(DEBIT|CREDIT)\s+.*$\n(\d{2}:\d{2} (?:AM|PM))\s+Transaction ID\s+(.*)\n\s+UTR No. (.*)\n\s+(?:Paid by|Credited to)\s+(.*)", re.MULTILINE)


@app.get("/")
async def root():
    return {"message": "Hello World"}

@app.post("/upload")
async def upload_file(file: UploadFile):
    reader = PdfReader(file.file)

    ext = '\n\n'.join([page.extract_text(extraction_mode="layout") for page in reader.pages])

    return {"filename": file.filename, "text": phone_pe.findall(ext)}
