mvn clean install
heroku login
heroku container:login
heroku container:push web -a mmariano-minesweeper --arg VERSION=0.0.1-SNAPSHOT
heroku container:release web -a mmariano-minesweeper