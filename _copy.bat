del /s /q bin\main > nul

xcopy build\classes\java\main bin\main /Q /H /Y /E
xcopy src\main\resources\*.* bin\main /Q /H /Y /E
