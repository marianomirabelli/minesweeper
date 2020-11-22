from MinesweeperClient import MinesweeperConnector
import random
import string

if __name__ == "__main__":

    print("Creating Random  user")
    letters = string.ascii_lowercase

    user = {
        'userName': ''.join(random.choice(letters) for i in range(10))
    }
    minesweeper_connector = MinesweeperConnector()
    userResponse = minesweeper_connector.create_user(user)

    newGame = {
        "rows": 5,
        "columns": 5,
        "mines": 5
    }

    print("Creating new Game")
    gameResponse = minesweeper_connector.create_game(newGame)
    gameStatus = gameResponse['status']
    id = gameResponse['id']
    while(gameStatus=='PLAYING'):
        try:

            gameMove = {
                "row":random.random() * 4 ,
                "column":random.random() * 4,
                "action": "FLIP"
            }
            response = minesweeper_connector.play_game(id,gameMove)
            gameStatus = response['status']
        except Exception as e:
              print(e)

    print('The user "%s has %s' %(user['userName'],gameStatus))

