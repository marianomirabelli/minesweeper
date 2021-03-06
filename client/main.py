from MinesweeperClient import MinesweeperConnector
import random
import string

def get_image(cell):

    dictionary = {"MINE": "*", "NUMBERED": str(cell['minesAround']), "BLANK": "B", "CLOSED": 'C'}
    return dictionary[cell['state']]

def print_board(board):

    print('\n'.join([''.join(['{:4}'.format(get_image(item)) for item in row])
                     for row in board]))


if __name__ == "__main__":

    rows = 10
    columns = 10
    mines = 15
    print("Creating Random  user")
    letters = string.ascii_lowercase

    user = {
        'userName': ''.join(random.choice(letters) for i in range(10))
    }
    minesweeper_connector = MinesweeperConnector()
    userResponse = minesweeper_connector.create_user(user)

    newGame = {
        "rows": rows,
        "columns": columns,
        "mines": mines
    }

    print("Creating new Game")
    game_response = minesweeper_connector.create_game(newGame)
    game_status = game_response['status']
    id = game_response['id']
    while(game_status=='PLAYING'):
        try:

            game_move = {
                "row":random.randint(0,rows-1) ,
                "column":random.randint(0,columns-1),
                "action": "FLIP"
            }
            print("Executing FLIP action at cell %s %s" % (game_move['row'], game_move['column']))
            response = minesweeper_connector.play_game(id, game_move)
            print_board(response['board']['cells'])
            game_status = response['status']
        except Exception as e:
            print('The FLIP action has failed at cell %s %s due to %s'%(game_move['row'],game_move['column'],e))

    game_response = minesweeper_connector.get_game(game_response['id'])
    print('The user "%s has %s the game id %s ' %(user['userName'],game_status,game_response['id']))



