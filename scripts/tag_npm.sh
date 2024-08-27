#!/bin/bash

# Check if there are uncommitted changes
if [[ -n $(git status --porcelain) ]]; then
  echo "There are uncommitted changes. Please commit or stash them before proceeding."
  exit 1
fi

# Bump the package version
echo "Choose the version type you want to increment (patch, minor, major):"
read version_type

yarn version --$version_type || exit 1

# Get the new version
new_version=$(cat package.json | grep version | head -1 | awk -F: '{ print $2 }' | tr -d '", ')

# Add changes and commit
git add package.json yarn.lock
git commit -m "Bump version to $new_version"

# Push to GitHub
git push origin-vivlio main

# Create a tag with the new version
git tag "v$new_version"
git push origin-vivlio "v$new_version"

npm publish

echo "Version $new_version successfully created and tagged on GitHub."
