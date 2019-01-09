#!/bin/bash

. ~/.virtualenvs/wereldontdooien/bin/activate
export DJANGO_SETTINGS_MODULE=project.settings

python -c '
from wereldontdooien.models import UnpublishedFonkel as Fonkel
try:
    print "Publishing “%s”" % Fonkel.objects.all()[0].publish()
except IndexError:
    print "There is nothing to publish!"
'
