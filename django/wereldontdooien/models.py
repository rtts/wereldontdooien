from django.db import models
from django.utils.html import strip_tags
from ckeditor.fields import RichTextField
from adminsortable.models import Sortable

class Moment(Sortable):
    aangemaakt = models.DateTimeField(auto_now_add=True)
    gewijzigd = models.DateTimeField(auto_now=True)
    gepubliceerd = models.DateTimeField(null=True, blank=True)
    zichtbaar = models.BooleanField(default=False)
    type = models.IntegerField(choices=(
            (1, "Spreuk"),
            (2, "Tip"),
            (3, "Persoonlijke opdracht"),
            (4, "Opdracht in gezelschap"),
            (5, "Kadootje"),
            (6, "Complimentje"),
            (7, "Overig"),
            ))
    inhoud = RichTextField()
    def __unicode__(self):
        return strip_tags(self.inhoud)
    class Meta(Sortable.Meta):
        verbose_name = "dagelijks geluksmomentje"
        verbose_name_plural = "dagelijkse geluksmomentjes"
