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


import os

import firebase_admin
from google.cloud import firestore
from json import dump, load
from Cat import Cat
from random import randint


"""
This script was designed with the intention of generating cats for my MMP project continuing the Feline Adoption 
Agency App for Android. With that in mind it utilizes a backend database from Google Firestore. For someone to use this
script they must change GOOGLE_SERVICES_ACCOUNT_KEY_LOCATION to reflect their own database.
"""


GOOGLE_SERVICES_ACCOUNT_KEY_LOCATION = ""
FILE_PATH_TO_COPY_OF_CATS = "CatCopies/copy.json"
CAT_DETAILS_DIR = "CatDetails"
CAT_NAMES_FILE = "CatNames.json"
CAT_BREEDS_FILE = "CatBreeds.json"
CAT_DESCRIPTIONS_FILE = "CatDescriptions.json"
CAT_COLOURS_FILE = "CatColours.json"
CAT_LOCATIONS_FILE = "CatLocations.json"
CAT_PICTURES_URLS_FILE = "CatPictureUrls.json"
MAX_CAT_AGE_IN_MONTHS = 100
MIN_CAT_AGE_IN_MONTHS = 0
CAT_ID_TO_START_FROM = 10
CAT_ID_TO_END_AT = 100


def load_cat_breeds():
    with open(os.path.join(CAT_DETAILS_DIR, CAT_BREEDS_FILE)) as breeds_file:
        return load(breeds_file)


def load_cat_descriptions():
    with open(os.path.join(CAT_DETAILS_DIR, CAT_DESCRIPTIONS_FILE)) as descriptions_file:
        return load(descriptions_file)


def load_cat_names():
    with open(os.path.join(CAT_DETAILS_DIR, CAT_NAMES_FILE)) as names_file:
        return load(names_file)


def load_colours():
    with open(os.path.join(CAT_DETAILS_DIR, CAT_COLOURS_FILE)) as colours_file:
        return load(colours_file)


def load_locations():
    with open(os.path.join(CAT_DETAILS_DIR, CAT_LOCATIONS_FILE)) as locations_file:
        return load(locations_file)


def load_picture_urls():
    with open(os.path.join(CAT_DETAILS_DIR, CAT_PICTURES_URLS_FILE)) as url_file:
        return load(url_file)


def load_cats_details():
    return load_cat_names(), load_cat_breeds(), load_cat_descriptions(), load_colours(), load_locations(), \
           load_picture_urls()


def generate_cats():
    cats_list = []

    cat_names, cat_breeds, cat_descriptions, colours, locations, picture_urls = load_cats_details()

    true_or_false = [True, False]
    male_or_female = [u"Male", u"Female"]

    for ii in range(CAT_ID_TO_START_FROM, CAT_ID_TO_END_AT):
        children = true_or_false[randint(0, 1)]
        cats_list.append(Cat(
            cat_name=cat_names[randint(0, len(cat_names) - 1)],
            cat_breed=cat_breeds[randint(0, len(cat_breeds) - 1)],
            description=cat_descriptions[randint(0, len(cat_descriptions) - 1)],
            cat_age_months=randint(a=MIN_CAT_AGE_IN_MONTHS, b=MAX_CAT_AGE_IN_MONTHS),
            cat_id=str(ii),
            colour=colours[randint(0, len(colours) - 1)],
            disabled=true_or_false[randint(0, 1)],
            neutered=true_or_false[randint(0, 1)],
            dogs=true_or_false[randint(0, 1)],
            indoors=true_or_false[randint(0, 1)],
            kids0to4=children,
            kids5to12=children,
            kids13to18=children,
            location=locations[randint(0, len(locations) - 1)],
            other_cats=true_or_false[randint(0, 1)],
            picture_url=picture_urls[randint(0, len(picture_urls) - 1)],
            sex=male_or_female[randint(0, 1)]
        ).to_dict())

    return cats_list


def upload_cats(cats_list):
    # Ensure that firestore is initialized:
    os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = GOOGLE_SERVICES_ACCOUNT_KEY_LOCATION
    firebase_admin.initialize_app()

    db = firestore.Client()

    for ii in range(CAT_ID_TO_START_FROM, CAT_ID_TO_END_AT):
        # Put each of the cats online in the correct file.
        db.collection("cats").document("cat"+str(ii)).set(cats_list[ii-CAT_ID_TO_START_FROM])


# This is the actual script that is ran, it subsequently calls all the functions
cats = generate_cats()
with open(FILE_PATH_TO_COPY_OF_CATS, "w+") as file:
    dump(cats, file)
upload_cats(cats)
