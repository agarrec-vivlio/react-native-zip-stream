#!/bin/bash

# Check if there are uncommitted changes
if [[ -n $(git status --porcelain) ]]; then
  echo "There are uncommitted changes. Please commit or stash them before proceeding."
  exit 1
fi

# Prompt for version type and validate input
valid_version_types=("patch" "minor" "major")
while true; do
  echo "Choose the version type you want to increment (patch, minor, major):"
  read version_type

  if [[ " ${valid_version_types[@]} " =~ " ${version_type} " ]]; then
    break
  else
    echo "Invalid version type. Please choose from patch, minor, or major."
  fi
done

# Bump the package version
yarn version --$version_type || exit 1

# Get the new version
new_version=$(grep -oP '(?<="version": ")[^"]*' package.json)

# Add changes and commit
git add package.json yarn.lock
git commit -m "Bump version to $new_version"

# Push to GitHub
git push origin master

# Create a tag with the new version
git tag "v$new_version"
git push origin "v$new_version"

npm publish

echo "Version $new_version successfully created, tagged on GitHub, and published to npm."
