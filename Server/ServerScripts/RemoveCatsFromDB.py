#    Copyright 2020 Samuel Jones
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.

"""
This script was made when I realised I made a mistake in my cat generation script for all cat ids between 10 and 99.
So this is designed at removing those cats. With that in mind it utilizes a backend database from Google Firestore.
For someone to use this script they must change GOOGLE_SERVICES_ACCOUNT_KEY_LOCATION to reflect their own database.
"""
import os

import firebase_admin
from google.cloud import firestore

GOOGLE_SERVICES_ACCOUNT_KEY_LOCATION = ""
CAT_ID_TO_START_FROM = 10
CAT_ID_TO_END_AT = 100

os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = GOOGLE_SERVICES_ACCOUNT_KEY_LOCATION
firebase_admin.initialize_app()

db = firestore.Client()

for ii in range(CAT_ID_TO_START_FROM, CAT_ID_TO_END_AT):
    db.collection("cats").document("cat" + str(ii)).delete()
