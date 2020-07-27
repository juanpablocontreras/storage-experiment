import mysql.connector

cnx_orig = mysql.connector.connect(
                               user='juan',
							   password='LapinCoquin13',
							   host='127.0.0.1',
							   database='EXP_ORIG')



orig_cursor = cnx_orig.cursor()

item = "a"
for s in range(2138):
    item = item + "a"

print(len(item))

for i in range(100):
	insert_row = f"INSERT INTO Large65535 (id,data_item) VALUES ({i},'{item}');"
	orig_cursor.execute(insert_row)

cnx_orig.commit()
orig_cursor.close()
cnx_orig.close()
