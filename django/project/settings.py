# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

SECRET_KEY = 'pakbta+b_wapi93&chh)o&2qwuufd2(zy^=4b^-&4hz8*isa)9'

DEBUG = True
TEMPLATE_DEBUG = True

ALLOWED_HOSTS = []

TEMPLATE_DIRS = (
    os.path.join(BASE_DIR, "templates"),
)

INSTALLED_APPS = (
    'ckeditor',
    'adminsortable',
    'django_extensions',
#    'grappelli',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'wereldontdooien'
)

GRAPPELLI_ADMIN_TITLE = 'Wereldontdooien - Beheer'
 
MIDDLEWARE_CLASSES = (
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
)

ROOT_URLCONF = 'project.urls'

WSGI_APPLICATION = 'project.wsgi.application'

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': 'wereldontdooien',
        'USER': 'wereldontdooien',
    }
}

LANGUAGE_CODE = 'nl'

TIME_ZONE = 'UTC'

USE_I18N = True

USE_L10N = True

USE_TZ = True

STATIC_URL = '/static/'
# STATICFILES_DIRS = (os.path.join(BASE_DIR, "static"),)
STATIC_ROOT = os.path.join(BASE_DIR, "static")

MEDIA_ROOT = '/srv/wereldontdooien'
CKEDITOR_UPLOAD_PATH = "/srv/wereldontdooien"

CKEDITOR_CONFIGS = {
    'default': {
        'autoGrow_onStartup': True,
        'forcePasteAsPlainText': True,
        'contentsCss': '/static/main.css', # sorry, should've computed this
        'toolbar_Full': [
            ['Format', 'Bold', 'Italic', 'Underline', 'RemoveFormat'],
            ['Link', 'Image', 'Blockquote', 'Table', 'HorizontalRule'],
            ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent'],
            ['Find', 'Replace', 'Scayt'],
            ['Source'],
            ],
        'toolbar': 'Full',
        'height': 400,
        'width': '100%'
        }
    }
