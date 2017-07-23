# otogame-updater

This program post the update of rhythm game scores to SNS.

## TODO

- [] Execute Lambda from triggers.
  - [] Execute regularly by CloudWatch.
  - [] Execute manually by API Gateway.
- [] Fetch playing-data from MyPage. (Lambda)
  - [] Login to MyPage by HTTP Client.
  - [] Fetch owned musics from music list.
  - [] Fetch playing-data of each difficulty from music details page.
- [] Insert or update playing-data. (Lambda/DynamoDB)
- [] Calculate diff of playing-data. (Lambda)
- [] If it was new record.
  - [] Tweet it.
