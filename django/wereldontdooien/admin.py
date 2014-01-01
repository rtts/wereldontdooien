from django.contrib import admin
from adminsortable.admin import SortableAdmin
from wereldontdooien.models import Moment

class CustomAdmin(SortableAdmin):
    list_display = ("zichtbaar", "aangemaakt", "gepubliceerd", "tekst", "type")
    list_display_links = ("tekst",)
    list_filter = ("type",)
    exclude = ("zichtbaar", "gepubliceerd")

admin.site.register(Moment, CustomAdmin)

# hide the following from admin:

from django.contrib.auth.models import User
from django.contrib.auth.models import Group

# admin.site.unregister(User)
admin.site.unregister(Group)

admin.site.disable_action('delete_selected')
