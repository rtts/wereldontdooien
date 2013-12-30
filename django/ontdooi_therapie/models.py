from django.db import models
from django.utils.html import strip_tags
from ckeditor.fiels import RichTextField
from adminsortable.models import Sortable

class Dagelijks_gelukmomentje(Sortable):
    aangemaakt = models.DateTimeField(auto_now_add=True)
    gewijzigd = models.DateTimeField(auto_now=True)
    gepubliceerd = models.DateTimeField(blank=True)
    zichtbaar = models.BooleanField(default=False)
    type = models.IntegerField(choices=(
            (1, "Spreuk"),
            (2, "Tip"),
            (3, "Persoonlijke opdracht"),
            (4, "Opdracht in gezelschap"),
            (5, "Kadootje"),
            (6, "Complimentje"),
            (7, "Overig"),
            )
    inhoud = RichTextField()
    def __unicode__(self):
        return strip_tags(self.inhoud)
    class Meta(Sortable.Meta):
        verbose_name_plural = "dagelijkse gelukmomentjes"
        pass
