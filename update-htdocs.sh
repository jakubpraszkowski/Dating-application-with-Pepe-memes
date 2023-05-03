#!/bin/bash

#Script updates htdocs folder everytime there is new commit on repo.

REPO_URL="https://github.com/jakubpraszkowski/Dating-application-with-Pepe-memes"
HTDOCS_DIR="~/Dating-application-with-Pepe-memes/htdocs"

git clone --depth 1 $REPO_URL /tmp/repo

rm -rf $HTDOCS_DIR/*
cp -r /tmp/repo/htdocs/* $HTDOCS_DIR/

rm -rf /tmp/repo
