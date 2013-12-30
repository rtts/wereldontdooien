from django.contrib import admin
from django.utils.html import strip_tags
from adminsortable.admin import SortableAdmin
from wereldontdooien.models import Moment

def inhoud(moment):
    return strip_tags(moment.inhoud)

class CustomAdmin(SortableAdmin):
    list_display = ('zichtbaar', 'aangemaakt', 'gepubliceerd', inhoud, "type")
    list_display_links = (inhoud,)
    list_filter = ('type',)

admin.site.register(Moment, CustomAdmin)

# hide the following from admin:

from django.contrib.auth.models import User
from django.contrib.sites.models import Site
from django.contrib.auth.models import Group

admin.site.unregister(User)
admin.site.unregister(Group)
# admin.site.unregister(Site)
