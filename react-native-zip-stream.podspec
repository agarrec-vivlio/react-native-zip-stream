Pod::Spec.new do |s|
  s.name         = 'react-native-zip-stream'
  s.version      = '1.0.0'
  s.summary      = 'A React Native module for streaming ZIP files.'
  s.description  = 'This module allows you to list and stream files from a ZIP archive in a React Native application.'
  s.homepage     = 'https://github.com/agarrec-vivlio/react-native-zip-stream'
  s.license      = { :type => 'MIT', :file => 'LICENSE' }
  s.author       = { 'Your Name' => 'your-email@example.com' }
  s.source       = { :git => 'https://github.com/agarrec-vivlio/react-native-zip-stream.git', :tag => s.version.to_s }
  s.source_files  = 'ios/**/*.{h,m,swift}'
  s.requires_arc = true
  s.dependency 'React'
  s.dependency 'ZipArchive'
end