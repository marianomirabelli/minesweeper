import requests
import json

class MinesweeperConnectorMeta(type):
    _instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls._instances:
            instance = super().__call__(*args, **kwargs)
            cls._instances[cls] = instance
        return cls._instances[cls]



class MinesweeperConnector(metaclass=MinesweeperConnectorMeta):
   __instance = None

   def __init__(self):
      self.URL = 'https://mmariano-minesweeper.herokuapp.com/api/minesweeper'
      self.cookies = []
      self.headers = {"cache-control": "no-cache", "Content-Type": "application/json"}

   def create_user(self,user_dto):
       user_path_url = '/users'
       final_url = self.URL+user_path_url
       r = requests.post(final_url, headers=self.headers, json=user_dto)
       body = json.loads(r.text)
       if r.status_code != 201:
           raise Exception(body['details'])
       self.cookies = r.cookies
       return body

   def create_game(self,game_dto):
       create_game_path_url = '/games'
       final_url = self.URL + create_game_path_url
       r = requests.post(final_url, headers=self.headers, json=game_dto, cookies=self.cookies)
       body = json.loads(r.text)
       if r.status_code != 201:
           raise Exception(body['details'])
       return body

   def play_game(self,id, action_dto):
       play_game_url = '/games/%s'
       get_game_url = play_game_url % id
       final_url = self.URL + get_game_url
       r = requests.patch(final_url, headers=self.headers, json=action_dto, cookies=self.cookies)
       body = json.loads(r.text)
       if r.status_code!=200:
           raise Exception(body['details'])
       return body

   def get_game(self,id):
       get_game_path = '/games/%s'
       get_game_url = get_game_path % id
       final_url = self.URL + get_game_url
       r = requests.get(final_url, headers=self.headers)
       body = json.loads(r.text)
       if r.status_code != 200:
           raise Exception(body['details'])
       return body








