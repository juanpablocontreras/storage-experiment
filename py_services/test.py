import mysql.connector

cnx_orig = mysql.connector.connect( user='juan',
							   password='LapinCoquin13',
							   host='127.0.0.1',
							   database='TWEETS_ORIG')

orig_cursor = cnx_orig.cursor()


for i in range(100):
	add_tweet = f"INSERT INTO Tweets (id,username,tweet) VALUES ({i},'Juan','Hello World{i}');"
	orig_cursor.execute(add_tweet)

cnx_orig.commit()
orig_cursor.close()
cnx_orig.close()
