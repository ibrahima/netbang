import cgi
import os
import hashlib

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.ext import db
from google.appengine.ext.webapp import template

class Server(db.Model):
    ip = db.StringProperty()
    gamename = db.StringProperty()
    type = db.StringProperty()
    date = db.DateTimeProperty(auto_now_add=True)
    
class MainPage(webapp.RequestHandler):
  def get(self):
    servers_query = Server.all().order('-date')
    servers = servers_query.fetch(10)

    template_values = {
      'servers': servers
      }

    path = os.path.join(os.path.dirname(__file__), 'list.xml')
    self.response.out.write(template.render(path, template_values))
    
class AddServer(webapp.RequestHandler):
  def get(self):
    self.response.out.write("""
      <html>
        <head>
        <title>Card Game Servers - Add Server</title>
        </head>
        <body>
          <form action="/sign" method="post">
            <div>
                Name: <input type="text" name="gamename"></input><br />
                IP Address: <input type="text" name="ip"></input><br />
                Game Type: <input type="text" name="type"></input><br />
                SHA-1 Hash: <input type="text" name="hash"></input><br />
            </div>
            <div><input type="submit" value="Add Server"></div>
          </form>
        </body>
      </html>""")


class ServerList(webapp.RequestHandler):
  def post(self):
      sh = hashlib.sha1()
      server = Server()
      server.ip = self.request.get('ip')
      server.gamename = self.request.get('gamename')
      server.type = self.request.get('type')
      sh.update(self.request.get('ip'))
      if sh.hexdigest()==self.request.get('hash'):
        q = db.GqlQuery("SELECT * FROM Server WHERE gamename = :gname", gname=server.gamename)
        results = q.fetch(1000)
        db.delete(results)
        server.put()
        self.response.out.write("Server added")
      else:
        self.response.out.write("Bad hash")

application = webapp.WSGIApplication(
                                     [('/', MainPage),
                                      ('/add', AddServer),
                                      ('/process', ServerList)],
                                     debug=True)

def main():
  run_wsgi_app(application)

if __name__ == "__main__":
  main()