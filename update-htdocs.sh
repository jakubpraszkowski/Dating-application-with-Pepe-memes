#!/bin/bash

#Script updates htdocs folder everytime there is new commit on repo.

REPO_URL="https://github.com/jakubpraszkowski/Dating-application-with-Pepe-memes"
HTDOCS_DIR="~/Dating-application-with-Pepe-memes/htdocs"

# Clone the latest commit from the repository
if git clone --depth 1 $REPO_URL /tmp/repo; then
    # Replace the contents of the htdocs directory with the contents of the cloned repository
    if rm -rf $HTDOCS_DIR/* && cp -r /tmp/repo/htdocs/* $HTDOCS_DIR/; then
        echo "htdocs directory updated successfully."
    else
        echo "Failed to copy cloned files to htdocs directory." >&2
        exit 1
    fi
else
    echo "Failed to clone repository." >&2
    exit 1
fi

# Clean up the temporary directory
if rm -rf /tmp/repo; then
    echo "Temporary directory cleaned up successfully."
else
    echo "Failed to clean up temporary directory." >&2
    exit 1
fi

