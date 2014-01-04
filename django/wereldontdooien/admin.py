from django.contrib import admin
from adminsortable.admin import SortableAdmin
from wereldontdooien.models import UnpublishedFonkel, PublishedFonkel

def publicatiedatum(fonkel):
    return fonkel.aangemaakt

class UnpublishedAdmin(SortableAdmin):
    list_display = ("aangemaakt", "gebruiker", "tekst", "type",)
    list_display_links = ("tekst",)
    list_filter = ("type", "gebruiker",)
    exclude = ("gebruiker",)
    def save_model(self, request, obj, form, change):
        if not obj.gebruiker:
            obj.gebruiker = request.user
        obj.save()
    class Media:
        css = { "all": ("moment_admin.css",)}

class PublishedAdmin(admin.ModelAdmin):
    list_display = ("id", "zichtbaar", publicatiedatum, "gebruiker", "tekst", "type",)
    list_display_links = ("tekst",)
    list_filter = ("type", "gebruiker",)
    fields = ("type", "tekst", "afbeelding",)
#    exclude = ("zichtbaar", "gepubliceerd")
    def has_add_permission(self, request, obj=None):
        return False    

    def has_delete_permission(self, request, obj=None):
        return False    

    class Media:
        css = { "all": ("moment_admin.css",)}

admin.site.register(UnpublishedFonkel, UnpublishedAdmin)
admin.site.register(PublishedFonkel, PublishedAdmin)

# hide the following from admin:
from django.contrib.auth.models import Group
admin.site.unregister(Group)
admin.site.disable_action('delete_selected')
