# coding: utf-8
import urllib.request
import sys,getopt
# If you are using Python 3+, import urllib instead of urllib2

import json 
import gzip





data =  {

        "Inputs": {

                "input1":
                {
                    "ColumnNames": ["fiyat", "il", "ilce", "mahalle", "krediyeUygun", "emlaktipi", "yil", "m2", "odasayisi", "banyosayisi", "binayasi", "binakatsayisi", "bulundugukat", "isitma", "kullanimdurumu", "siteicinde", "kimden"],
                    "Values": [ 
					[ "0", sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5], sys.argv[6], sys.argv[7], sys.argv[8], sys.argv[9], sys.argv[10], sys.argv[11], sys.argv[12], sys.argv[13], sys.argv[14], sys.argv[15], sys.argv[16] ] ]
                },        },
            "GlobalParameters": {
        "Append score columns to output": "True",
}
    }

	
body = str.encode(json.dumps(data))

url = 'https://ussouthcentral.services.azureml.net/workspaces/4448f01b579546e39b07a730a0774600/services/af85449a300345f1a0ddcb9f205093a5/execute?api-version=2.0&details=true'
api_key = 'abc123' # Replace this with the API key for the web service
headers = {'Content-Type':'application/json;charset=utf-8', 'Authorization':('Bearer '+ 'grh2aawJgjFBrt0/YmQNHOE9zzbDROE5Ft3RSnG9snjZEZzAYexNESEaIykG+YLdAvx3q+rEolHKP1WufYqy6g==')}

req = urllib.request.Request(url, body, headers) 

try:

    response = urllib.request.urlopen(req)

    # If you are using Python 3+, replace urllib2 with urllib.request in the above code:
    # req = urllib.request.Request(url, body, headers) 
    # response = urllib.request.urlopen(req)

    result = response.read()
	
    print(result) 
	#print(a)

except error:
    print("The request failed with status code: " + str(error.code))

    # Print the headers - they include the requert ID and the timestamp, which are useful for debugging the failure
    print(error.info())

    print(json.loads(error.read()))                 