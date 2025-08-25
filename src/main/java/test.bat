@echo off
REM Compile the Client.java once

echo Starting 20 clients in parallel...

REM Loop from 1 to 20
for /L %%i in (1,1,20) do (
    start "Client-%%i" cmd /c java Client.java
)

echo All clients started.
pause