# otogame-updater

This program post the update of rhythm game scores to SNS.

## TODO

### cbRev

- General
  - [x] Login to MyPage by HTTP Client
- [Feature] Update user data
  - [Lmabda] UpdateUserData
	- [x] Fetch user data
    - [x] Configure DynamoDB
	- [x] Insert user data to DynamoDB
  - [Lambda] UpdateUserDataStream
	- [x] Configure DynamoDB streams
	- [ ] Receive streams
	- If RankPoint is rising
	  - [ ] Tweet it
  - Execute `UpdateUserData` from triggers
	- [x] Execute regularly by CloudWatch
	- [ ] Execute manually by API Gateway
- [Feature] Fetch playing data from MyPage
  - [Lambda] UpdateUserPlayingData
    - [ ] Fetch owned musics from music list
    - [ ] Fetch playing data of each difficulty from music details page
	- [ ] Configure DynamoDB
	- [ ] Insert or update playing data
  - [Lambda] UpdateUserPlayingDataStream
	- [ ] Configure DynamoDB streams
	- [ ] Receive streams
	- [ ] Calculate diff of playing data
	- If it was new record
      - [ ] Tweet it
  - Execute `UpdateUserPlayingData` from triggers
	- [ ] Execute regularly by CloudWatch
	- [ ] Execute manually by API Gateway
