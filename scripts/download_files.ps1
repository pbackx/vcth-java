# Set the S3 bucket and local directory paths
$bucket = "vcthackathon-data"
$path = ""
$localDir = "I:\valdata"

# Get the list of files from the S3 bucket
$files = aws s3 ls "s3://$bucket/$path" --recursive | ForEach-Object {
    $_.Split(" ", [System.StringSplitOptions]::RemoveEmptyEntries)[-1]
}

# Loop through each file and replace the semicolon in the filename with an underscore
foreach ($file in $files) {
    $newFile = $file -replace ":", "_"
    $localPath = Join-Path $localDir $newFile

    # Ensure the directory exists locally
    $localDirectory = Split-Path $localPath
    if (-not (Test-Path $localDirectory)) {
        New-Item -ItemType Directory -Path $localDirectory | Out-Null
    }

    # Copy the file from S3 to the local directory with the updated filename if it does not already exist
    if (-not (Test-Path $localPath)) {
        aws s3 cp "s3://$bucket/$file" "$localPath"
    } else {
        Write-Output "File $localPath already exists. Skipping..."
    }
}
