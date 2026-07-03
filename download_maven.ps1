$ErrorActionPreference = "Stop"
Write-Output "Downloading Maven..."
$url = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
$zipPath = "maven.zip"
Invoke-WebRequest -Uri $url -OutFile $zipPath
Write-Output "Extracting Maven..."
Expand-Archive -Path $zipPath -DestinationPath "maven"
Remove-Item $zipPath
Write-Output "Maven setup complete."
