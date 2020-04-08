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


class Cat(object):
    def __init__(self, cat_age_months, cat_breed, cat_name, colour, description, disabled, location, neutered,
                 picture_url, dogs, indoors, kids0to4, kids13to18, kids5to12, other_cats, sex, cat_id):
        self.cat_age_months = cat_age_months
        self.cat_breed = cat_breed
        self.cat_name = cat_name
        self.colour = colour
        self.description = description
        self.disabled = disabled
        self.location = location
        self.neutered = neutered
        self.picture_url = picture_url
        self.dogs = dogs
        self.indoors = indoors
        self.kids0to4 = kids0to4
        self.kids13to18 = kids13to18
        self.kids5to12 = kids5to12
        self.other_cats = other_cats
        self.sex = sex
        self.cat_id = cat_id

    def to_dict(self):
        return {
            u"catAgeMonths": self.cat_age_months,
            u"catBreed": self.cat_breed,
            u"catName": self.cat_name,
            u"colour": self.colour,
            u"description": self.description,
            u"disabled": self.disabled,
            u"location": self.location,
            u"neutered": self.neutered,
            u"pictureUrl": self.picture_url,
            u"dogs": self.dogs,
            u"indoors": self.indoors,
            u"kids0to4": self.kids0to4,
            u"kids13to18": self.kids13to18,
            u"kids5to12": self.kids5to12,
            u"otherCats": self.other_cats,
            u"sex": self.sex,
            u"catId": self.cat_id
        }
