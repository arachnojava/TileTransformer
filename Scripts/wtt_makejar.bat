@echo off

echo Calling MHFramework.bat...
call MHFramework.bat

rem copy ..\bin\*.class .
copy .\Scripts\wtt_mainClass.txt .\bin

cd bin

C:\"Program Files (x86)"\Java\jdk1.6.0_22\bin\jar cvfm WTT.jar wtt_mainClass.txt mhframework *.class
rem pause
