#!/bin/bash

# Loop through all .ttf files in the current directory
for file in *.ttf; do
    # Convert the filename to lowercase and replace "-" with "_"
    new_name=$(echo "$file" | tr '[:upper:]' '[:lower:]' | tr '-' '_')

    # Rename the file only if the name has changed
    if [[ "$file" != "$new_name" ]]; then
        mv "$file" "$new_name"
        echo "Renamed: $file -> $new_name"
    fi
done

echo "Renaming complete!"
