#!/bin/sh



    : '
    Sends curl request to GitHub to notify about build stages progress
    Pass data in tis argument order only
    Arguments required are
    GITHUB_USERNAME
    GITHUB_ACCESS_TOKEN
    GITHUB_URL
    REPO_OWNER_NAME
    REPO_NAME
    ISSUE_ID
    MESSAGE

    '
    # curl -u "shashanp:c2227ae0a98cc911d5c89007842d4a31a8ca743d" -X POST -d '{"comment" : "test comment"}'
    # https://sqbu-github.cisco.com/api/v3/repos/shashanp/test-trigger-project/issues/2/comments
    # for label curl -u "shashanp:c2227ae0a98cc911d5c89007842d4a31a8ca743d" -X POST -d '["2nd jan test from curl"]'
    # https://sqbu-github.cisco.com/api/v3/repos/shashanp/test-trigger-project/issues/2/labels

    GITHUB_USERNAME=$1
    GITHUB_ACCESS_TOKEN=$2
    GITHUB_URL=$3
    REPO_OWNER_NAME=$4
    REPO_NAME=$5
    ISSUE_ID=$6
    MESSAGE="$7"

    API_URL=$GITHUB_URL/api/v3/repos/$REPO_OWNER_NAME/$REPO_NAME/issues/$ISSUE_ID/labels

    echo $GITHUB_USERNAME,$GITHUB_ACCESS_TOKEN,$GITHUB_URL
    echo $REPO_OWNER_NAME,$REPO_NAME,$ISSUE_ID,$MESSAGE
    echo $API_URL
    echo curl -u "$GITHUB_USERNAME:$GITHUB_ACCESS_TOKEN" -X POST -d  "[\"$MESSAGE\"]" "$API_URL"
    curl -u "$GITHUB_USERNAME:$GITHUB_ACCESS_TOKEN" -X POST -d  "[\"$MESSAGE\"]" "$API_URL"

# notfiyGit "shashanp" "c2227ae0a98cc911d5c89007842d4a31a8ca743d" "https://sqbu-github.cisco.com" "shashanp" "test-trigger-project" 2 "this works i guess"


