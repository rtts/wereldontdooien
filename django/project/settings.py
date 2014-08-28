# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
import os
BASE_DIR = os.path.dirname(os.path.dirname(__file__))

SECRET_KEY = 'pakbta+b_wapi93&chh)o&2qwuufd2(zy^=4b^-&4hz8*isa)9'

DEBUG = True
TEMPLATE_DEBUG = DEBUG

ALLOWED_HOSTS = []

TEMPLATE_DIRS = (
    os.path.join(BASE_DIR, "templates"),
)

INSTALLED_APPS = (
    'adminsortable',
    'django_extensions',
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'wereldontdooien'
)
 
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

USE_TZ = True
TIME_ZONE = 'UTC'

LANGUAGE_CODE = 'nl'
USE_I18N = True
USE_L10N = True
LOCALE_PATHS = (os.path.join(BASE_DIR, "locale"),)

STATIC_URL = '/static/'
STATICFILES_DIRS = (os.path.join(BASE_DIR, "files"),)
STATIC_ROOT = os.path.join(BASE_DIR, "static")

MEDIA_ROOT = '/srv/wereldontdooien'
MEDIA_URL = '/media/'
