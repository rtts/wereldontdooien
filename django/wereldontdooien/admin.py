from django.contrib import admin
from adminsortable.admin import SortableAdmin
from wereldontdooien.models import Fonkel

def gepubliceerd(moment):
    return moment.gepubliceerd or ""

def nummer(moment):
    return moment.order

class CustomAdmin(SortableAdmin):
    list_display = (nummer, "zichtbaar", "aangemaakt", gepubliceerd, "tekst", "type")
    list_display_links = ("tekst",)
    list_filter = ("type",)
    exclude = ("zichtbaar", "gepubliceerd")
    class Media:
        css = { "all": ("moment_admin.css",)}

admin.site.register(Fonkel, CustomAdmin)

# hide the following from admin:

from django.contrib.auth.models import User
from django.contrib.auth.models import Group

# admin.site.unregister(User)
admin.site.unregister(Group)

admin.site.disable_action('delete_selected')
